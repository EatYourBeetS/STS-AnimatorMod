package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.animator.beta.special.Suigintou_BlackFeather;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnPurgeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.DesecrationPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Suigintou extends AnimatorCard implements OnPurgeSubscriber
{
    public static final EYBCardData DATA = Register(Suigintou.class)
    		.SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental).SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Suigintou_BlackFeather(), false));
    
    public Suigintou()
    {
        super(DATA);

        Initialize(5, 0, 1, 1);
        SetUpgrade(2, 0, 0);
        SetAffinity_Blue(2, 0, 0);
        SetAffinity_Dark(2, 0, 0);

        SetEthereal(true);
        SetExhaust(true);
        SetUnique(true, true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 6 == 1)
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void triggerOnManualDiscard()
    {
        GameActions.Top.MakeCardInHand(new Suigintou_BlackFeather());

        super.triggerOnManualDiscard();
    }

    @Override
    public void OnPurge(AbstractCard card, CardGroup source) {
        if (card != null && this.uuid.equals(card.uuid)) {
            GameActions.Top.MakeCardInHand(new Suigintou_BlackFeather());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.DARKNESS);

        GameActions.Bottom.ChannelOrbs(Dark::new, secondaryValue).AddCallback(() -> {
            for (AbstractOrb orb : player.orbs) {
                if (Dark.ORB_ID.equals(orb.ID)) {
                    GameUtilities.ModifyOrbBaseEvokeAmount(orb, magicNumber + GameUtilities.GetPowerAmount(DesecrationPower.POWER_ID), true, false);
                    GameUtilities.ModifyOrbBasePassiveAmount(orb, magicNumber + GameUtilities.GetPowerAmount(DesecrationPower.POWER_ID), true, false);
                }
            }
            GameActions.Bottom.TriggerOrbPassive(JUtils.Count(player.hand.group, GameUtilities::IsHindrance)).SetFilter(o -> Dark.ORB_ID.equals(o.ID));
        });

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        CombatStats.onPurge.Subscribe(this);
    }
}
