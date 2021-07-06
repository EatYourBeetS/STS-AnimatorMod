package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class HousakiTohya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HousakiTohya.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal);
    static
    {
        DATA.AddPreview(new HousakiMinori(), false);
    }

    public HousakiTohya()
    {
        super(DATA);

        Initialize(5, 0, 1, 3);
        SetUpgrade(3, 0, 0, 0);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.LogHorizon);
        SetAffinity(1, 0, 0, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.ApplyVulnerable(player, m, magicNumber);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Draw(1)
        .SetFilter(c -> HousakiMinori.DATA.ID.equals(c.cardID), false)
        .AddCallback(() ->
        {
            if (HasTeamwork(secondaryValue))
            {
                GameActions.Bottom.GainEnergy(1);
            }
        });
    }
}