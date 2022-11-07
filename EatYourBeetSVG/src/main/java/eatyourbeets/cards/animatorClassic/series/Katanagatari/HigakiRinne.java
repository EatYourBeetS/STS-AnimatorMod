package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.actions.animatorClassic.HigakiRinneAction;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.animator.status.Status_Slimed;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.BlockAttribute;
import eatyourbeets.cards.base.attributes.DamageAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.interfaces.delegates.ActionT1;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.powers.animatorClassic.HigakiRinnePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.WeightedList;

public class HigakiRinne extends AnimatorClassicCard implements OnAddToDeckListener
{
    private static final WeightedList<ActionT1<HigakiRinne>> drawActions = new WeightedList<>();
    private static final WeightedList<ActionT1<HigakiRinne>> exhaustActions = new WeightedList<>();
    public static final EYBCardData DATA = Register(HigakiRinne.class).SetSeriesFromClassPackage()
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None)
            .PostInitialize(data ->
            {
                drawActions.Add(c -> { }, 10);
                drawActions.Add(c -> GameActions.Bottom.SFX(HigakiRinneAction.GetRandomSFX(), 0.6f, 1.6f), 20);
                drawActions.Add(c -> c.ChangeForm(CardType.ATTACK), 10);
                drawActions.Add(c -> c.ChangeForm(CardType.POWER), 10);
                drawActions.Add(c ->
                {
                    GameActions.Bottom.WaitRealtime(0.3f);
                    GameActions.Bottom.MakeCardInHand(c.makeStatEquivalentCopy());
                }, 1);
                drawActions.Add(c ->
                {
                    GameActions.Bottom.WaitRealtime(0.3f);
                    GameActions.Bottom.MakeCardInHand(ThrowingKnife.GetRandomCardInBattle());
                }, 5);
                drawActions.Add(c ->
                {
                    GameActions.Top.Draw(1);
                    GameActions.Top.MoveCard(c, player.hand, player.discardPile).ShowEffect(true, false);
                }, 5);

                exhaustActions.Add(c -> { }, 10);
                exhaustActions.Add(c -> GameActions.Bottom.SFX(HigakiRinneAction.GetRandomSFX(), 0.6f, 1.6f), 10);
                exhaustActions.Add(c -> GameActions.Bottom.MakeCardInHand(ThrowingKnife.GetRandomCardInBattle()), 5);
                exhaustActions.Add(c -> GameActions.Bottom.MakeCardInHand(c.makeStatEquivalentCopy()), 1);
                exhaustActions.Add(c -> GameActions.Bottom.MakeCardInHand(new Status_Slimed()), 5);
            });;

    public HigakiRinne()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetAttackType(EYBAttackType.Normal);
        
        SetShapeshifter();
    }

    @Override
    public boolean OnAddToDeck()
    {
        SFX.Play(HigakiRinneAction.GetRandomSFX(), 0.6f, 1.6f);

        return true;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return type == CardType.SKILL ? BlockAttribute.Instance.SetCard(this).SetText("?", Settings.CREAM_COLOR) : null;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return type == CardType.ATTACK ? DamageAttribute.Instance.SetCard(this).SetText("!", Settings.CREAM_COLOR) : null;
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

        drawActions.Retrieve(rng, false).Invoke(this);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        exhaustActions.Retrieve(rng, false).Invoke(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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
            GameActions.Bottom.Wait(0.2f);
            GameActions.Bottom.Add(new HigakiRinneAction(this));
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

            GameActions.Bottom.SFX(HigakiRinneAction.GetRandomSFX());
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
            this.target = CardTarget.SELF;
        }
        else
        {
            LoadImage(null);
            this.type = CardType.SKILL;
            this.target = CardTarget.ALL;
        }
    }
}