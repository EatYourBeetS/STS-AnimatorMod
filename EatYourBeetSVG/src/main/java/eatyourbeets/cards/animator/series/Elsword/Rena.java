package eatyourbeets.cards.animator.series.Elsword;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Rena extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Rena.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            DATA.AddPreview(knife, false);
        }
    }

    public Rena()
    {
        super(DATA);

        Initialize(0, 3, 0, 2);
        SetUpgrade(0, 3);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.Elsword);
        SetAffinity(0, 2, 0, 0, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainBlur(secondaryValue);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.CreateThrowingKnives(1);

        if (HasSynergy() && CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainBlur(secondaryValue);
        }
    }
}