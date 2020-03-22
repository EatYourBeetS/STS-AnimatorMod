 package eatyourbeets.console.commands;

 import basemod.DevConsole;
 import basemod.devcommands.ConsoleCommand;
 import com.megacrit.cardcrawl.cards.AbstractCard;
 import com.megacrit.cardcrawl.cards.CardGroup;
 import com.megacrit.cardcrawl.core.AbstractCreature;
 import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
 import com.megacrit.cardcrawl.helpers.CardLibrary;
 import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
 import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
 import com.megacrit.cardcrawl.ui.DialogWord;
 import eatyourbeets.actions.monsters.TalkAction;
 import eatyourbeets.cards.base.AnimatorCard;
 import eatyourbeets.cards.base.EYBCard;
 import eatyourbeets.cards.base.Synergies;
 import eatyourbeets.cards.base.Synergy;
 import eatyourbeets.interfaces.markers.MartialArtist;
 import eatyourbeets.interfaces.markers.Spellcaster;
 import eatyourbeets.resources.GR;
 import eatyourbeets.resources.animator.misc.AnimatorLoadout;
 import eatyourbeets.ui.CustomCardLibSortHeader;
 import eatyourbeets.utilities.*;

 import java.util.ArrayList;

 public class ParseGenericCommand extends ConsoleCommand
 {
     private static FieldInfo<Boolean> _isDeltaMultiplied;
     private static FieldInfo<Float> _deltaMultiplier;

     public ParseGenericCommand()
     {
         this.minExtraTokens = 1;
         this.maxExtraTokens = 99;
         this.simpleCheck = true;
     }

     @Override
     protected void execute(String[] tokens, int depth)
     {
         try
         {
             if (tokens.length > 1)
             {
                 if (tokens[1].equals("starter") && tokens.length > 2)
                 {
                     String loadoutName = tokens[2].replace("_", " ");
                     AnimatorLoadout loadout = GR.Animator.Data.GetByName(loadoutName);
                     if (GameUtilities.InGame() && loadout != null && AbstractDungeon.player != null && AbstractDungeon.player.masterDeck != null)
                     {
                         AbstractDungeon.player.masterDeck.group.removeAll(AbstractDungeon.player.masterDeck.getPurgeableCards().group);

                         for (String cardID : loadout.GetStartingDeck())
                         {
                             AbstractDungeon.player.masterDeck.group.add(CardLibrary.getCard(cardID).makeCopy());
                         }

                         DevConsole.log("Replaced starting deck with: " + loadoutName);
                     }
                     else
                     {
                         DevConsole.log("Error processing command.");
                     }

                     return;
                 }
                 if (tokens[1].equals("get-series") && tokens.length > 2)
                 {
                     String loadoutName = tokens[2].replace("_", " ");
                     AnimatorLoadout loadout = GR.Animator.Data.GetByName(loadoutName);

                     if (GameUtilities.InGame() && loadout != null && AbstractDungeon.player != null && AbstractDungeon.player.masterDeck != null) {

                         Synergy synergy = Synergies.GetByID(loadout.ID);

                         AbstractDungeon.player.masterDeck.group.removeAll(AbstractDungeon.player.masterDeck.getPurgeableCards().group);

                         for (AbstractCard c : CardLibrary.getAllCards()) {
                                 if ((((AnimatorCard) c).synergy == synergy)) {
                                    AbstractCard c_upgraded = c.makeCopy();
                                    c_upgraded.upgrade();

                                     AbstractDungeon.player.masterDeck.group.add(c);
                                     AbstractDungeon.player.masterDeck.group.add(c_upgraded);
                                 }
                         }

                         DevConsole.log("Replaced deck with all cards from: " + loadoutName);
                     }
                     else
                     {
                         DevConsole.log("Error processing command.");
                     }

                     return;
                 }

                 if (tokens[1].equals("sort-by-tribe"))
                 {
                     CustomCardLibSortHeader.Instance.group.group.sort((a, b) ->
                     {
                         int aValue = 0;
                         if (a instanceof Spellcaster)
                         {
                             aValue = 1;
                         }
                         else if (a instanceof MartialArtist)
                         {
                             aValue = 2;
                         }
                         else if (a instanceof AnimatorCard && ((AnimatorCard)a).anySynergy)
                         {
                             aValue = 3;
                         }

                         int bValue = 0;
                         if (b instanceof Spellcaster)
                         {
                             bValue = 1;
                         }
                         else if (b instanceof MartialArtist)
                         {
                             bValue = 2;
                         }
                         else if (b instanceof AnimatorCard && ((AnimatorCard)b).anySynergy)
                         {
                             bValue = 3;
                         }

                         return Integer.compare(aValue, bValue);
                     });
                     return;
                 }

                 if (tokens[1].equals("set-zoom"))
                 {
                     GR.Animator.Config.SetCropCardImages(tokens.length > 2 && tokens[2].equals("true"), true);
                     return;
                 }

                 if (tokens[1].equals("show-upgrades"))
                 {
                     SingleCardViewPopup.isViewingUpgrade ^= true;
                     return;
                 }

                 if (tokens[1].equals("crop"))
                 {
                     DevConsole.log("This command is currently not available.");
                     return;
                 }

                 if (tokens[1].equals("talk"))
                 {
                     float duration = 2;
                     if (tokens.length > 2)
                     {
                         duration = JavaUtilities.ParseFloat(tokens[2], duration);
                     }

                     RandomizedList<DialogWord.AppearEffect> effects = new RandomizedList<>();
                     effects.Add(DialogWord.AppearEffect.BUMP_IN);
                     effects.Add(DialogWord.AppearEffect.FADE_IN);
                     effects.Add(DialogWord.AppearEffect.GROW_IN);
                     effects.Add(DialogWord.AppearEffect.NONE);

                     for (AbstractCreature creature : GameUtilities.GetAllCharacters(true))
                     {
                         GameActions.Bottom.Add(new TalkAction(creature, "Lorem ipsum dolor sit amet, consectetur adipiscing elit.", duration, duration))
                         .SetEffect(effects.Retrieve(AbstractDungeon.miscRng));
                     }

                     return;
                 }

                 if (tokens[1].equals("show-special"))
                 {
                     CustomCardLibSortHeader.ShowSpecial = tokens.length > 2 && tokens[2].equals("true");
                     return;
                 }

                 if (tokens[1].equals("remove-colorless"))
                 {
                     final FieldInfo<CardGroup> _colorless = JavaUtilities.GetField("colorlessCards", CardLibraryScreen.class);
                     if (CustomCardLibSortHeader.Screen != null)
                     {
                         _colorless.Get(CustomCardLibSortHeader.Screen).group.removeIf(card -> card.rarity == AbstractCard.CardRarity.BASIC || !(card instanceof EYBCard));
                     }

                     DevConsole.log("Removed cards from colorless library.");
                     return;
                 }

                 Test(tokens);
             }
         }
         catch (Exception ex)
         {
             DevConsole.log("Error: " + ex.getClass().getSimpleName());
         }
     }

     @Override
     public ArrayList<String> extraOptions(String[] tokens, int depth)
     {
         ArrayList<String> suggestions = new ArrayList<>();

         if (tokens.length > 1 && tokens.length <= 3)
         {
             if (tokens[1].equals("starter") || tokens[1].equals("get-series"))
             {
                 for (AnimatorLoadout loadout : GR.Animator.Data.GetEveryLoadout())
                 {
                     suggestions.add(loadout.Name.replace(" ", "_"));
                 }
             }
         }

         return suggestions;
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
