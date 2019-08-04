package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.cards.colorless.MasterOfStrategy;
import com.megacrit.cardcrawl.cards.colorless.MindBlast;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.cards.animator.HigakiRinne;
import eatyourbeets.interfaces.AllowedUnnamedReignRelic;
import eatyourbeets.relics.animator.ExquisiteBloodVial;

import java.util.ArrayList;

public class UnnamedRelicEquipEffect extends AbstractGameEffect
{
    private final int goldBonus;

    private int apparitionsCount = 0;

    public UnnamedRelicEquipEffect(int goldBonus)
    {
        this.goldBonus = goldBonus;
        this.duration = 1.0F;
    }

    private void ReplaceCard(ArrayList<AbstractCard> replacement, String cardID)
    {
        UnlockTracker.markCardAsSeen(cardID);
        replacement.add(CardLibrary.getCard(cardID).makeCopy());
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        ModHelper.setModsFalse();

        ArrayList<AbstractCard> replacement = ReplaceCards(p);

        int hp = CalculateMaxHealth();
        if (hp < 999 && apparitionsCount > 1)
        {
            hp *= 1 - (0.1f * (apparitionsCount - 1));

            if (hp < 10)
            {
                hp = 10;
            }
        }

        p.gold = goldBonus;
        p.maxHealth = p.currentHealth = hp;
        p.healthBarUpdatedEvent();

        p.potionSlots = AbstractDungeon.ascensionLevel < 11 ? 3 : 2;
        p.potions.clear();
        for (int i = 0; i < p.potionSlots; i++)
        {
            p.potions.add(new PotionSlot(i));
        }

        p.adjustPotionPositions();

        p.masterDeck.clear();
        p.masterDeck.group.addAll(replacement);

        for (AbstractRelic relic : p.relics)
        {
            if (relic instanceof AllowedUnnamedReignRelic)
            {
                ((AllowedUnnamedReignRelic)relic).OnEquipUnnamedReignRelic();
            }
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb)
    {

    }

    public void dispose()
    {

    }

    public static int CalculateMaxHealth()
    {
        CharSelectInfo info = AbstractDungeon.player.getLoadout();
        int hp = 100;
        if (info != null)
        {
            hp = info.maxHp;
        }

        if (hp <= 60)
        {
            hp = 80;
        }
        else if (hp <= 100)
        {
            hp = 100;
        }
        else if (hp <= 120)
        {
            hp = 120;
        }
        else if (hp < 999)
        {
            hp = 150;
        }

        return hp;
    }

    public static int CalculateGoldBonus()
    {
        AbstractPlayer p = AbstractDungeon.player;

        int bonus = 60;
        for (AbstractRelic r : p.relics)
        {
            if (!(r instanceof AllowedUnnamedReignRelic))
            {
                if (r instanceof ExquisiteBloodVial)
                {
                    bonus += 30 + ((r.counter > 0) ? (r.counter * 10) : 0);
                }
                else switch (r.tier)
                {
                    case STARTER:
                        bonus += 0;
                        break;

                    case COMMON:
                        bonus += 6;
                        break;

                    case UNCOMMON:
                        bonus += 10;
                        break;

                    case RARE:
                        bonus += 18;
                        break;

                    case SPECIAL:
                        bonus += 25;
                        break;

                    case BOSS:
                        bonus += 30;
                        break;

                    case SHOP:
                        bonus += 10;
                        break;
                }
            }
        }

        for (AbstractCard c : p.masterDeck.group)
        {
            bonus += Math.min(c.timesUpgraded, 20) * 5;
        }

        for (AbstractPotion potion : p.potions)
        {
            switch (potion.rarity)
            {
                case PLACEHOLDER:
                    bonus += 0;
                    break;

                case COMMON:
                    bonus += 6;
                    break;

                case UNCOMMON:
                    bonus += 10;
                    break;

                case RARE:
                    bonus += 14;
                    break;
            }
        }

        bonus += p.maxHealth / 2;
        bonus += p.gold / 7;

        return Math.min(999, bonus);
    }

    private ArrayList<AbstractCard> ReplaceCards(AbstractPlayer p)
    {
        apparitionsCount = 0;

        ArrayList<AbstractCard> replacement = new ArrayList<>();
        for (AbstractCard card : p.masterDeck.group)
        {
            switch (card.cardID)
            {
                case "infinitespire:Virus":
                {
                    ReplaceCard(replacement, Anger.ID);
                    ReplaceCard(replacement, HigakiRinne.ID);
                    break;
                }

                case "hubris:Fate":
                {
                    ReplaceCard(replacement, Discovery.ID);
                    break;
                }

                case "hubris:Rewind":
                {
                    ReplaceCard(replacement, EchoForm.ID);
                    break;
                }

                case "hubris:InfiniteBlow":
                {
                    ReplaceCard(replacement, SearingBlow.ID);
                    break;
                }

                case "infinitespire:Gouge":
                {
                    ReplaceCard(replacement, BeamCell.ID);
                    ReplaceCard(replacement, Neutralize.ID);
                    break;
                }

                case "infinitespire:SevenWalls":
                {
                    ReplaceCard(replacement, Dash.ID);
                    break;
                }

                case "infinitespire:Fortify":
                {
                    ReplaceCard(replacement, ReinforcedBody.ID);
                    break;
                }

                case "infinitespire:Punishment":
                {
                    ReplaceCard(replacement, MindBlast.ID);
                    break;
                }

                case "infinitespire:FutureSight":
                {
                    ReplaceCard(replacement, SeeingRed.ID);
                    break;
                }

                case "infinitespire:Oblivion":
                {
                    ReplaceCard(replacement, Chaos.ID);
                    break;
                }

                case "infinitespire:DeathsTouch":
                {
                    ReplaceCard(replacement, Bludgeon.ID);
                    break;
                }

                case "infinitespire:NeuralNetwork":
                {
                    ReplaceCard(replacement, MachineLearning.ID);
                    break;
                }

                case "infinitespire:Execution":
                {
                    ReplaceCard(replacement, Terror.ID);
                    break;
                }

                case "infinitespire:Menacing":
                {
                    ReplaceCard(replacement, Apparition.ID);
                    ReplaceCard(replacement, Apparition.ID);
                    apparitionsCount += 2;
                    break;
                }

                case "infinitespire:TheBestDefense": // This card...
                {
                    ReplaceCard(replacement, Flex.ID);
                    ReplaceCard(replacement, Apparition.ID);
                    ReplaceCard(replacement, Apparition.ID);
                    apparitionsCount += 2;
                    break;
                }

                case "infinitespire:UltimateForm":
                {
                    ReplaceCard(replacement, Inflame.ID);
                    ReplaceCard(replacement, Defragment.ID);
                    ReplaceCard(replacement, Footwork.ID);
                    break;
                }

                case "infinitespire:Collect":
                case "infinitespire:Haul":
                {
                    ReplaceCard(replacement, MasterOfStrategy.ID);
                    break;
                }

                case "ReplayTheSpireMod:Black Plague":
                {
                    ReplaceCard(replacement, Malaise.ID);
                    ReplaceCard(replacement, NoxiousFumes.ID);
                    break;
                }

                case "ReplayTheSpireMod:??????????????????????":
                {
                    ReplaceCard(replacement, HigakiRinne.ID);
                    break;
                }

                case GeneticAlgorithm.ID:
                {
                    AbstractCard copy = card.makeCopy();

                    copy.baseBlock = copy.misc = 12;
                    copy.initializeDescription();
                    replacement.add(copy);

                    break;
                }

                case Apparition.ID:
                {
                    apparitionsCount += 1;
                    replacement.add(card.makeCopy());
                    break;
                }

                case Necronomicurse.ID:
                {
                    break;
                }

                default:
                {
                    replacement.add(card.makeCopy());
                }
            }
        }

        return replacement;
    }
}