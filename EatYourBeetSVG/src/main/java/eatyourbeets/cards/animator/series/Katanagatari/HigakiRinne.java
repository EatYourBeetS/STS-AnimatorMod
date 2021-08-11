package eatyourbeets.cards.animator.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.actions.animator.HigakiRinneAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.powers.animator.HigakiRinnePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class HigakiRinne extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HigakiRinne.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public HigakiRinne()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAttackType(EYBAttackType.Normal);
        SetAffinity_Star(2, 0, 1);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return type == CardType.SKILL ? BlockAttribute.Instance.SetCard(this).SetText("?", Settings.CREAM_COLOR) : null;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return type == CardType.ATTACK ? DamageAttribute.Instance.SetCard(this).SetText("?", Settings.CREAM_COLOR) : null;
    }

    @Override
    public AbstractCard makeStatEquivalentCopy()
    {
        final HigakiRinne copy = JUtils.SafeCast(super.makeStatEquivalentCopy(), HigakiRinne.class);
        if (copy != null)
        {
            copy.ChangeForm(type);
        }

        return copy;
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
            GameActions.Top.MoveCard(this, player.hand, player.discardPile).ShowEffect(true, false);
        }
        else if (n < 49)
        {
            GameActions.Bottom.SFX(GameUtilities.GetRandomElement(sounds), 0.6f, 1.6f);
        }
        else if (n < 59)
        {
            ChangeForm(CardType.ATTACK);
        }
        else if (n < 69)
        {
            ChangeForm(CardType.POWER);
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
            GameActions.Bottom.SFX(GameUtilities.GetRandomElement(sounds), 0.6f, 1.6f);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (this.type == CardType.POWER)
        {
            GameActions.Bottom.StackPower(new HigakiRinnePower(p, this, upgraded ? 2 : 1));
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
                GameActions.Bottom.Add(new DamageAction(m, info, AttackEffects.POISON, true));
            }
        }
        else if (n < 10)
        {
            int d = upgraded ? 20 : 15;

            GameActions.Bottom.Wait(0.35f);
            GameActions.Bottom.VFX(new BorderFlashEffect(Color.RED));
            GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_EVOKE, 0.5f, 0.6f);
            GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));

            GameActions.Bottom.DealDamage(p, m, 1, DamageInfo.DamageType.THORNS, AttackEffects.NONE);

            GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.HIGH));
            GameActions.Bottom.WaitRealtime(0.6f);

            GameActions.Bottom.DealDamage(p, m, rng.random(10, d), DamageInfo.DamageType.THORNS, AttackEffects.POISON);
        }
        else
        {
            int d = upgraded ? 8 : 6;

            GameActions.Bottom.DealDamage(p, m, rng.random(2, d), DamageInfo.DamageType.THORNS, AttackEffects.POISON);
            GameActions.Bottom.DealDamage(p, m, rng.random(2, d), DamageInfo.DamageType.THORNS, AttackEffects.POISON);

            GameActions.Bottom.SFX(GameUtilities.GetRandomElement(sounds));
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
        sounds.add(SFX.VO_AWAKENEDONE_3);
        sounds.add(SFX.VO_GIANTHEAD_1B);
        sounds.add(SFX.VO_GREMLINANGRY_1A);
        sounds.add(SFX.VO_GREMLINCALM_2A);
        sounds.add(SFX.VO_GREMLINFAT_2A);
        sounds.add(SFX.VO_GREMLINNOB_1B);
        sounds.add(SFX.VO_HEALER_1A);
        sounds.add(SFX.VO_MERCENARY_1B);
        sounds.add(SFX.VO_MERCHANT_MB);
        sounds.add(SFX.VO_SLAVERBLUE_2A);
        sounds.add(SFX.THUNDERCLAP);
        sounds.add(SFX.BELL);
        sounds.add(SFX.ENEMY_TURN);
        sounds.add(SFX.DEATH_STINGER);
        sounds.add(SFX.BOSS_VICTORY_STINGER);
        sounds.add(SFX.TINGSHA);
        sounds.add(SFX.NECRONOMICON);
        sounds.add(SFX.INTIMIDATE);
    }
}