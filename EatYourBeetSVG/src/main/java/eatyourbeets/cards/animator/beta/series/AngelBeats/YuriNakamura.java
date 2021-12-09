package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
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
        SetAffinity_Light(1, 0, 2);
        SetAffinity_Orange(0, 0, 1);
        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 3);
        SetHitCount(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ExhaustFromHand(name, magicNumber, false).SetOptions(true, true, true).AddCallback(cards -> {
            GameActions.Bottom.Heal(Math.min(cards.size() * secondaryValue, GameActionManager.playerHpLastTurn - player.currentHealth));
        });

        if (player.exhaustPile.size() > 0 && (info.IsSynergizing || TrySpendAffinity(Affinity.Light))) {
            GameActions.Last.Motivate(player.exhaustPile).SetFilter(AfterLifeMod::IsAdded);
            GameActions.Last.Motivate(player.exhaustPile).SetFilter(AfterLifeMod::IsAdded);
        }
    }
}