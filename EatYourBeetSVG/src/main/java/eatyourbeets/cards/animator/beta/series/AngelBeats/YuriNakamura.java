package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;

public class YuriNakamura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YuriNakamura.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public YuriNakamura()
    {
        super(DATA);

        Initialize(4, 8, 2, 5);
        SetUpgrade(1, 2,0, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Light(2, 0, 0);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 3);
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
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ExhaustFromHand(name, magicNumber, false).SetOptions(true, true, true).AddCallback(cards -> {
            GameActions.Bottom.Heal(Math.min(cards.size() * secondaryValue, GameActionManager.playerHpLastTurn - player.currentHealth));
        });

        if (CheckAffinity(Affinity.Light) || isSynergizing) {
            GameActions.Bottom.Motivate(player.exhaustPile).SetFilter(AfterLifeMod::IsAdded);
        }
    }
}