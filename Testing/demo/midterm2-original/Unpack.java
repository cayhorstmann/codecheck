import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.util.regex.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;

public class Unpack
{
   private int sid;
   private Pattern subpattern = Pattern.compile("\u2329[^\u2223\u232A]*(\u2223[^\u2223\u232A]*)*\u232A");

   public static void main(String[] args) throws Exception
   {
      Unpack un = new Unpack();
      if (args.length == 0) un.unpack();
      else if (args.length == 1) un.unpack(args[0]);
      else un.unpack(args[0], args[1]);
   }

   public void unpack() throws Exception
   {
      unpack(JOptionPane.showInputDialog("Passphrase"));
   }

   public void unpack(String password) throws Exception
   {
      for (String file : new File(".").list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
               return name.endsWith(".ser");
            }
         })) 
         unpack(password, file.substring(0, file.length() - 4));      
   }

   public void unpack(String password, String filename) throws Exception
   {
      if (new File(filename).exists()) 
      {
         System.out.println(filename + " exists--not overwritten");
         return;
      }

      ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename + ".ser"));
      Item item = (Item) in.readObject();
      in.close();

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      KeySpec spec = new PBEKeySpec(password.toCharArray(), item.salt, 65536, 128);
      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

      SecretKey tmp = factory.generateSecret(spec);
      SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
      cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(item.iv));
      byte[] plaintext = null;
      try
      {
         plaintext = cipher.doFinal(item.ciphertext);
      }
      catch (Exception ex)
      {
         JOptionPane.showMessageDialog(null, "Wrong password");
         throw ex;
      }

      MessageDigest alg = MessageDigest.getInstance("SHA-1");
      alg.update(plaintext);
      byte[] hash = alg.digest();
      if (!Arrays.equals(hash, item.hash))
      {
         JOptionPane.showMessageDialog(null, "Wrong password");
         throw new IllegalStateException("hash mismatch");
      }

      FileOutputStream out = new FileOutputStream(filename);
      try
      {
         if (isText(plaintext))
         {
            String contents = new String(plaintext, StandardCharsets.UTF_8);
            contents = substitute(contents);
            plaintext = contents.getBytes(StandardCharsets.UTF_8);
            alg = MessageDigest.getInstance("SHA-1");
            alg.update(plaintext);
            hash = alg.digest();
         }

         try
         {
            out.write(plaintext);
         }
         finally
         {
            out.close();
            
         }

         FileInputStream in2 = new FileInputStream(filename);
         try
         {
            byte[] data = new byte[1024];
            alg = MessageDigest.getInstance("SHA-1");
            int nread = 0; 
 
            while ((nread = in2.read(data)) != -1) 
               alg.update(data, 0, nread);
            if (!Arrays.equals(alg.digest(), hash))
               throw new IllegalStateException("hash mismatch");
         }
         finally
         {
            in2.close();
         }
      }
      catch (Exception ex)
      {
         JOptionPane.showMessageDialog(null, "Could not save " + filename);
         ex.printStackTrace();
         new File(filename).delete();
      }
   }

   public String substitute(String text)
   {
      Matcher matcher = subpattern.matcher(text);
      if (!matcher.find()) return text;
      if (sid == 0)
      {
         sid = Integer.parseInt(JOptionPane.showInputDialog("Student ID:"));
         if (sid == 0) throw new IllegalArgumentException("sid is 0");
      }

      boolean done = false;
      int previous = 0;
      StringBuilder result = new StringBuilder();
      do
      {
         int start = matcher.start();
         int end = matcher.end();
         result.append(text.substring(previous, start));
         previous = end;
         if (start + 2 == end) // 〈〉
            result.append("" + sid);
         else
         {
            String[] choices = text.substring(start + 1, end - 1).split("\u2223");
            result.append(choices[sid % choices.length]);
         }
      }
      while (matcher.find());
      result.append(text.substring(previous));
      return result.toString();
   }

   public boolean isText(byte[] contents)
   {
      for (byte b : contents)
      {
         if (0 <= b && b < 32 && "\t\n\r\f".indexOf((char) b) == -1)
            return false;
      }
      return true;
   }

   static class Item implements java.io.Serializable
   {
      byte[] salt;
      byte[] iv;
      byte[] ciphertext;
      byte[] hash;
   }
}
