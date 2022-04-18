package eatyourbeets.cards_beta.LogHorizon;

import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActions;

public class HousakiTohya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HousakiTohya.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new HousakiMinori(), false));

    public HousakiTohya()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(3, 0, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.General, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_VERTICAL);
        GameActions.Bottom.ApplyVulnerable(player, m, magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(1)
        .SetFilter(c -> HousakiMinori.DATA.ID.equals(c.cardID), false)
        .AddCallback(() ->
        {
            if (CheckAffinity(Affinity.General))
            {
                GameActions.Bottom.GainEnergy(1);
            }
        });
    }
}