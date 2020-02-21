 package eatyourbeets.console.commands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.MethodInfo;

import java.io.IOException;
import java.util.ArrayList;

 public class SetGameSpeed extends ConsoleCommand
 {
     private static MethodInfo _writeConfig;
     private static FieldInfo<SpireConfig> _config;
     private static FieldInfo<Boolean> _isDeltaMultiplied;
     private static FieldInfo<Float> _deltaMultiplier;

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

             _config = JavaUtilities.GetField("config", c);
             _writeConfig = JavaUtilities.GetMethod("writeConfig", c);
             _isDeltaMultiplied = JavaUtilities.GetField("isDeltaMultiplied", c);
             _deltaMultiplier = JavaUtilities.GetField("deltaMultiplier", c);

             return true;
         }
         catch (RuntimeException | ClassNotFoundException ignored)
         {
             return false;
         }
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
                     _isDeltaMultiplied.Set(null, false);

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

                     _deltaMultiplier.Set(null, speed);
                     _isDeltaMultiplied.Set(null, true);

                     DevConsole.log("Set game speed at: " + (speed * 100) + "%");
                 }

                 _writeConfig.Invoke(null);
                 _config.Get(null).save();
             }
             catch (IOException ex)
             {
                 DevConsole.log("Could not save config.");
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
