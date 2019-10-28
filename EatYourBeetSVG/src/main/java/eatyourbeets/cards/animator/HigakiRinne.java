package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.resources.Resources_Animator;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.actions.common.WaitRealtimeAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.HigakiRinnePower;

import java.util.ArrayList;

public class HigakiRinne extends AnimatorCard
{
    public static final String ID = Register(HigakiRinne.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Drawn, EYBCardBadge.Discard, EYBCardBadge.Exhaust, EYBCardBadge.Special);

    public HigakiRinne()
    {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0,0,2);

        SetSynergy(Synergies.Katanagatari, true);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        HigakiRinne c = Utilities.SafeCast(super.makeStatEquivalentCopy(), HigakiRinne.class);
        if (c != null)
        {
            c.ChangeForm(type);
        }

        return c;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (this.type != CardType.SKILL)
        {
            ChangeForm(CardType.SKILL);
        }

        int n = AbstractDungeon.cardRandomRng.random(100);
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
        else if (n < 38)
        {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8f));
            AbstractDungeon.actionManager.addToBottom(new DiscardSpecificCardAction(this, AbstractDungeon.player.hand));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        }
        else if (n < 45)
        {
            AbstractDungeon.actionManager.addToBottom(new SFXAction(Utilities.GetRandomElement(sounds)));
        }
        else if (n < 55)
        {
            ChangeForm(CardType.ATTACK);
        }
        else if (n < 65)
        {
            ChangeForm(CardType.POWER);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        int n = AbstractDungeon.cardRandomRng.random(100);
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
            int stacks = upgraded ? 2 : 1;
            GameActionsHelper.ApplyPower(p, p, new HigakiRinnePower(p, this, stacks), stacks);
        }
        else if (this.type == CardType.ATTACK)
        {
            AttackAction(p, m);
        }
        else
        {
            for (int i = 0; i < this.magicNumber; i++)
            {
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2f));
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

    private void AttackAction(AbstractPlayer p, AbstractMonster m)
    {
        Random rng = AbstractDungeon.cardRandomRng;

        int n = rng.random(15);
        if (n < 5)
        {
            int count = upgraded ? 20 : 15;
            for (int i = 0; i < count; i++)
            {
                int damage = rng.random(1);
                DamageInfo info = new DamageInfo(p, damage, DamageInfo.DamageType.THORNS);
                GameActionsHelper.AddToBottom(new DamageAction(m, info, AbstractGameAction.AttackEffect.POISON, true));
            }
        }
        else if (n < 10)
        {
            int d = upgraded ? 20 : 15;

            GameActionsHelper.AddToBottom(new WaitAction(0.35f));
            GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
            GameActionsHelper.AddToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.5f));
            GameActionsHelper.AddToBottom(new VFXAction(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4.0F, m.hb.cY - m.hb.height / 4.0F)));
            GameActionsHelper.DamageTarget(p, m, 1, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);
            GameActionsHelper.AddToBottom(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            GameActionsHelper.AddToBottom(new WaitRealtimeAction(0.8f));
            GameActionsHelper.DamageTarget(p, m, rng.random(10, d), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON);
        }
        else
        {
            int d = upgraded ? 8 : 6;

            GameActionsHelper.DamageTarget(p, m, rng.random(2,d), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON);
            GameActionsHelper.DamageTarget(p, m, rng.random(2,d), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON);
            GameActionsHelper.AddToBottom(new SFXAction(Utilities.GetRandomElement(sounds)));
        }
    }

    private void ChangeForm(CardType type)
    {
        if (type == CardType.ATTACK)
        {
            this.loadCardImage(Resources_Animator.GetCardImage(ID + "Attack"));
            this.type = CardType.ATTACK;
            this.target = CardTarget.ENEMY;
        }
        else if (type == CardType.POWER)
        {
            this.loadCardImage(Resources_Animator.GetCardImage(ID + "Power"));
            this.type = CardType.POWER;
            this.target = CardTarget.ALL;
        }
        else
        {
            this.loadCardImage(Resources_Animator.GetCardImage(ID));
            this.type = CardType.SKILL;
            this.target = CardTarget.ALL;
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