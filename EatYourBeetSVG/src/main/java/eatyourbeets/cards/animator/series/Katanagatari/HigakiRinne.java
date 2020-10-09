package eatyourbeets.cards.animator.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.animator.HigakiRinnePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class HigakiRinne extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HigakiRinne.class).SetSkill(0, CardRarity.RARE, EYBCardTarget.None);

    public HigakiRinne()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAttackType(EYBAttackType.Normal);
        SetSynergy(Synergies.Katanagatari);
        SetShapeshifter();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        HigakiRinne c = JUtils.SafeCast(super.makeStatEquivalentCopy(), HigakiRinne.class);
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

        int n = rng.random(100);
        if (n < 3)
        {
            GameActions.Bottom.WaitRealtime(0.3f);
            GameActions.Bottom.MakeCardInHand(makeStatEquivalentCopy());
        }
        else if (n < 12)
        {
            GameActions.Bottom.WaitRealtime(0.3f);
            GameActions.Bottom.MakeCardInHand(new Shiv());
        }
        else if (n < 38)
        {
            GameActions.Top.Draw(1);
            GameActions.Top.MoveCard(this, player.hand, player.discardPile);
            GameActions.Top.WaitRealtime(0.3f);
        }
        else if (n < 45)
        {
            GameActions.Bottom.SFX(JUtils.GetRandomElement(sounds));
        }
        else if (n < 55)
        {
            ChangeForm(CardType.ATTACK);
        }
        else if (n < 65)
        {
            ChangeForm(CardType.POWER);
        }
        else if (n < 69)
        {
            CardCrawlGame.sound.playA("ORB_SLOT_GAIN", -0.82f);
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        int n = rng.random(100);
        if (n < 20)
        {
            GameActions.Bottom.MakeCardInHand(new Shiv());
        }
        else if (n < 40)
        {
            GameActions.Bottom.MakeCardInHand(new Madness());
        }
        else if (n < 60)
        {
            GameActions.Bottom.MakeCardInHand(new Slimed());
        }
        else if (n < 64)
        {
            GameActions.Bottom.MakeCardInHand(makeStatEquivalentCopy());
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.type == CardType.POWER)
        {
            int stacks = upgraded ? 2 : 1;
            GameActions.Bottom.StackPower(new HigakiRinnePower(p, this, stacks));
        }
        else if (this.type == CardType.ATTACK)
        {
            AttackAction(p, m);
        }
        else
        {
            for (int i = 0; i < magicNumber; i++)
            {
                GameActions.Bottom.Wait(0.2f);
                GameActions.Bottom.Add(new HigakiRinneAction(this));
            }
        }
    }

    private void AttackAction(AbstractPlayer p, AbstractMonster m)
    {
        int n = rng.random(15);
        if (n < 5)
        {
            int count = upgraded ? 20 : 15;
            for (int i = 0; i < count; i++)
            {
                int damage = rng.random(1);
                DamageInfo info = new DamageInfo(p, damage, DamageInfo.DamageType.THORNS);
                GameActions.Bottom.Add(new DamageAction(m, info, AbstractGameAction.AttackEffect.POISON, true));
            }
        }
        else if (n < 10)
        {
            int d = upgraded ? 20 : 15;

            GameActions.Bottom.Wait(0.35f);
            GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
            GameActions.Bottom.SFX("ORB_LIGHTNING_EVOKE", 0.5f);
            GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));

            GameActions.Bottom.DealDamage(p, m, 1, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);

            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            GameActions.Bottom.WaitRealtime(0.6f);

            GameActions.Bottom.DealDamage(p, m, rng.random(10, d), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON);
        }
        else
        {
            int d = upgraded ? 8 : 6;

            GameActions.Bottom.DealDamage(p, m, rng.random(2, d), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON);
            GameActions.Bottom.DealDamage(p, m, rng.random(2, d), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON);

            GameActions.Bottom.SFX(JUtils.GetRandomElement(sounds));
        }
    }

    private void ChangeForm(CardType type)
    {
        if (type == CardType.ATTACK)
        {
            LoadImage("Attack");
            this.type = CardType.ATTACK;
            this.target = CardTarget.ENEMY;
        }
        else if (type == CardType.POWER)
        {
            LoadImage("Power");
            this.type = CardType.POWER;
            this.target = CardTarget.ALL;
        }
        else
        {
            LoadImage(null);
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