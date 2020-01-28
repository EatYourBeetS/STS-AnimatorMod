 package eatyourbeets.console.commands;

 import basemod.DevConsole;
 import basemod.devcommands.ConsoleCommand;
 import com.badlogic.gdx.graphics.Color;
 import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
 import eatyourbeets.utilities.FieldInfo;

 import java.util.ArrayList;

 public class ParseGenericCommand extends ConsoleCommand
 {
     private static FieldInfo<Boolean> _isDeltaMultiplied;
     private static FieldInfo<Float> _deltaMultiplier;

     public static SpireConfig config;

     public ParseGenericCommand()
     {
         this.minExtraTokens = 1;
         this.maxExtraTokens = 1;
         this.simpleCheck = true;
     }

     @Override
     protected void execute(String[] tokens, int depth)
     {
         if (depth == 1)
         {
             try
             {
                Test(tokens[depth]);
             }
             catch (Exception ex)
             {
                 DevConsole.log("Error: " + ex.getClass().getSimpleName());
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

     public static void Test(String text)
     {
         String[] values = text.split(",");
         if (values.length == 3)
         {
             COLOR.r = Integer.parseInt(values[0]) / 100f;
             COLOR.g = Integer.parseInt(values[1]) / 100f;
             COLOR.b = Integer.parseInt(values[2]) / 100f;
         }
     }

     public static Color COLOR = Color.WHITE.cpy();
 }
