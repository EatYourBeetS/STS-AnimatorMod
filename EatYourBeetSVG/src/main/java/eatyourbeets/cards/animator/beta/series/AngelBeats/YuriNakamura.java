package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
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
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Light(2, 0, 2);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 3);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ExhaustFromHand(name, magicNumber, false).SetOptions(true, true, true).AddCallback(cards -> {
            GameActions.Bottom.Heal(Math.min(cards.size() * secondaryValue, GameActionManager.playerHpLastTurn - player.currentHealth));
        });

        if (player.exhaustPile.size() > 0 && (TrySpendAffinity(Affinity.Light) || info.IsSynergizing)) {
            GameActions.Last.Motivate(player.exhaustPile).SetFilter(AfterLifeMod::IsAdded);
            GameActions.Last.Motivate(player.exhaustPile).SetFilter(AfterLifeMod::IsAdded);
        }
    }
}