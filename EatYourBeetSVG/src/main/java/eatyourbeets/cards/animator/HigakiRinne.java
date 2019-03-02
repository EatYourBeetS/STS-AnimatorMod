package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.actions.HigakiRinneAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.HigakiRinnePower;

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
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        int n = AbstractDungeon.miscRng.random(100);
        if (n < 3)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.6f));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(makeCopy()));
        }
        else if (n < 12)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.6f));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Shiv()));
        }
        else if (n < 34)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8f));
            AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(this, AbstractDungeon.player.hand));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        }
        else if (n < 42)
        {
            AbstractDungeon.actionManager.addToBottom(new SFXAction(Utilities.GetRandomElement(sounds)));
        }
        else if (n < 55)
        {
            this.loadCardImage(AnimatorResources.GetCardImage(ID + "Attack"));
            this.type = CardType.ATTACK;
            this.target = CardTarget.ENEMY;
        }
        else if (n < 68)
        {
            this.loadCardImage(AnimatorResources.GetCardImage(ID + "Power"));
            this.type = CardType.POWER;
            this.target = CardTarget.ALL;
        }
        else if (this.type != CardType.SKILL)
        {
            this.loadCardImage(AnimatorResources.GetCardImage(ID));
            this.type = CardType.SKILL;
            this.target = CardTarget.ALL;
        }
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
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        if (this.type == CardType.POWER)
        {
            GameActionsHelper.ApplyPower(p, p, new HigakiRinnePower(p, this, upgraded ? 2 : 1), 2);
        }
        else if (this.type == CardType.ATTACK)
        {
            int count = upgraded ? 20 : 15;
            for (int i = 0; i < count; i++)
            {
                int damage = AbstractDungeon.miscRng.random(1);
                GameActionsHelper.DamageTarget(p, m, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON);
            }

            this.loadCardImage(AnimatorResources.GetCardImage(ID));
            this.type = CardType.SKILL;
            this.target = CardTarget.ALL;
        }
        else
        {
            for (int i = 0; i < this.magicNumber; i++)
            {
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.6f));
                GameActionsHelper.AddToBottom(new HigakiRinneAction(this));
            }
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