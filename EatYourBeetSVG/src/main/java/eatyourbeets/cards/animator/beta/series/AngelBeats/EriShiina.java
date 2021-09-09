package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class EriShiina extends AnimatorCard
{
    public static final EYBCardData DATA = Register(EriShiina.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal).SetSeriesFromClassPackage().PostInitialize(data ->
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            data.AddPreview(knife, true);
        }
    });

    public EriShiina()
    {
        super(DATA);

        Initialize(6, 0, 3, 2);
        SetUpgrade(3, 0, 0, 1);

        SetAffinity_Green(2, 0, 4);
        SetExhaust(true);
        AfterLifeMod.Add(this);

        SetAffinityRequirement(Affinity.Green, 3);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);

        if (CombatStats.ControlPile.Contains(this))
        {
            GameActions.Bottom.CreateThrowingKnives(magicNumber);
        }

        final AbstractCard last = GameUtilities.GetLastCardPlayed(true, 1);
        if (isSynergizing && last != null && (last.cost <= 0 || last.costForTurn <= 0))
        {
            GameActions.Bottom.GainAgility(2,true);
        }

        if (CheckAffinity(Affinity.Green)) {
            GameActions.Bottom.GainBlur(1);
        }
    }
}