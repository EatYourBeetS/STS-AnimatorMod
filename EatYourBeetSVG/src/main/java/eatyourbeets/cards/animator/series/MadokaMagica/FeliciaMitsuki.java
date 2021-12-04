package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class FeliciaMitsuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(FeliciaMitsuki.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public FeliciaMitsuki()
    {
        super(DATA);

        Initialize(8, 0, 3, 1);
        SetUpgrade(3, 0, 0, 0);

        SetAffinity_Red(1,0,0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(0,0,1);

        SetAffinityRequirement(Affinity.Orange, 3);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (IsStarter()) {
            amount += (JUtils.Count(player.hand.group, GameUtilities::IsHindrance)) * magicNumber;
        }
        return super.ModifyDamage(enemy, amount);
    }
    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (CheckAffinity(Affinity.Orange)) {
            GameActions.Bottom.PurgeFromPile(name,1,player.exhaustPile).SetFilter(GameUtilities::IsHindrance).AddCallback(cards -> {
                if (cards.size() > 0) {
                    GameActions.Bottom.GainEndurance(secondaryValue);
                }
            });
        }
    }
}