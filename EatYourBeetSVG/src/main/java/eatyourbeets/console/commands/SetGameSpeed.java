 package eatyourbeets.console.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;

 public class SetGameSpeed extends ConsoleCommand
 {
     private static FieldInfo<Boolean> isDeltaMultiplied;
     private static FieldInfo<Float> deltaMultiplier;

     public static SpireConfig config;

     public SetGameSpeed()
     {
         this.minExtraTokens = 1;
         this.maxExtraTokens = 1;
         this.simpleCheck = true;
     }

     public static boolean TryInitialize()
     {
         try
         {
             Class<?> c = Class.forName("skrelpoid.superfastmode.SuperFastMode");

             isDeltaMultiplied = JavaUtilities.GetField("isDeltaMultiplied", c);
             deltaMultiplier = JavaUtilities.GetField("deltaMultiplier", c);

             return true;
         }
         catch (RuntimeException | ClassNotFoundException e)
         {
             e.printStackTrace();
         }

         return false;
     }

     @Override
     protected void execute(String[] tokens, int depth)
     {
         if (depth == 1)
         {
             try
             {
                 if (tokens[1].equals("off"))
                 {
                     isDeltaMultiplied.Set(null, false);

                     DevConsole.log("Speed multiplier off");
                 }
                 else
                 {
                     float speed = Integer.parseInt(tokens[1]) / 100f;
                     if (speed <= 0)
                     {
                         speed = 0.5f;
                     }
                     else if (speed > 10)
                     {
                         speed = 10f;
                     }

                     deltaMultiplier.Set(null, speed);
                     isDeltaMultiplied.Set(null, true);

                     DevConsole.log("Set game speed at: " + (speed * 100) + "%");
                 }
             }
             catch (NumberFormatException ex)
             {
                 DevConsole.log("Invalid game speed.");
             }
         }
     }

     @Override
     public ArrayList<String> extraOptions(String[] tokens, int depth)
     {
         if (depth >= 2)
         {
             complete = true;
         }

         return new ArrayList<>();
     }
 }
