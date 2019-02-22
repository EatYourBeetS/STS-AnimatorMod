package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class HigakiRinne extends AnimatorCard
{
    public static final String ID = CreateFullID(HigakiRinne.class.getSimpleName());

    public HigakiRinne()
    {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(2,2,2);

        SetSynergy(Synergies.Katanagatari, true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        int n = AbstractDungeon.miscRng.random(100);
        if (n < 20)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Shiv()));
        }
        else if (n < 40)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Madness()));
        }
        else if (n < 60)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Slimed()));
        }
        else if (n < 64)
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(makeCopy()));
        }
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int n = AbstractDungeon.miscRng.random(100);
        if (n < 3)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(1f));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(makeCopy()));
        }
        else if (n < 12)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(1f));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Shiv()));
        }
        else if (n < 35)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(1f));
            AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(this, AbstractDungeon.player.hand));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(1));
            ExecuteRandomAction(p, AbstractDungeon.miscRng);
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    private void ExecuteRandomAction(AbstractPlayer p, Random rng)
    {
        int n = rng.random(97);
        logger.info("Rinne says: " + n);
        if (n < 10) // 10%
        {
            for (int i = 0; i < 3; i++)
            {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
            }
        }
        else if (n < 20) // 10%
        {
            for (int i = 0; i < 3; i++)
            {
                AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(p, this.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
            }
        }
        else if (n < 25) // 5%
        {
            GameActionsHelper.DrawCard(p, 1);
        }
        else if (n < 30) // 5%
        {
            GameActionsHelper.AddToBottom(new UpgradeRandomCardAction());
        }
        else if (n < 35) // 5%
        {
            GameActionsHelper.AddToBottom(new ApplyPoisonOnRandomMonsterAction(p, 5, false, AbstractGameAction.AttackEffect.POISON));
        }
        else if (n < 40) // 5%
        {
            GameActionsHelper.GainEnergy(1);
        }
        else if (n < 45) // 5%
        {
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, 1), 1);
        }
        else if (n < 50) // 5%
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
        }
        else if (n < 55) // 5%
        {
            GameActionsHelper.ApplyPower(p, p, new IntangiblePlayerPower(p, 1), 1);
        }
        else if (n < 60) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
        }
        else if (n < 65) // 5%
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, 2, false), 2));
        }
        else if (n < 70) // 5%
        {
            AbstractMonster m = AbstractDungeon.getRandomMonster();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, 2, false), 2));
        }
        else if (n < 75) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Shiv()));
        }
        else if (n < 80) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Madness()));
        }
        else if (n < 85) // 5%
        {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Slimed()));
        }
        else if (n < 90) // 5%
        {
            AbstractCard card = CardLibrary.getRandomColorSpecificCard(this.color, rng);
            if (!card.tags.contains(CardTags.HEALING))
            {
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
            }
        }
        else if (n < 95)
        {
            AbstractDungeon.actionManager.addToBottom(new SFXAction(Utilities.GetRandomElement(sounds)));
        }
        else
        {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, cardStrings.EXTENDED_DESCRIPTION[0], 1.0F, 2.0F));
        }
    }

    private static final ArrayList<String> sounds = new ArrayList<>();

    static
    {
        sounds.add("VO_AWAKENEDONE_3");
        sounds.add("VO_GIANTHEAD_1B");
        sounds.add("VO_GREMLINANGRY_1A");
        sounds.add("VO_GREMLINCALM_2A");
        sounds.add("VO_GREMLINFAT_2A");
        sounds.add("VO_GREMLINNOB_1B");
        sounds.add("VO_HEALER_1A");
        sounds.add("VO_MERCENARY_1B");
        sounds.add("VO_MERCHANT_MB");
        sounds.add("VO_SLAVERBLUE_2A");
        sounds.add("THUNDERCLAP");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("BELL");
        sounds.add("NECRONOMICON");
        sounds.add("INTIMIDATE");
    }
}