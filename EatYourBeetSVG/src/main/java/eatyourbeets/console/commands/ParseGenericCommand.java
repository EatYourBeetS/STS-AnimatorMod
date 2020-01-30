 package eatyourbeets.console.commands;

 import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import eatyourbeets.utilities.FieldInfo;
 import eatyourbeets.utilities.Testing;

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
         if (tokens.length > 1)
         {
             try
             {
                Test(tokens);
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
         return new ArrayList<>();
     }

     public static void Test(String[] tokens) throws NumberFormatException
     {
         Float[] values = new Float[tokens.length-1];
         for (int i = 0; i < values.length; i++)
         {
             values[i] = Float.parseFloat(tokens[i+1]);
         }

         Testing.SetValues(values);
     }
 }
