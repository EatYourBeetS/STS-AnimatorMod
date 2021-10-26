package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import eatyourbeets.cards.animator.status.Status_Wound;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class Yuuichirou_Asuramaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Yuuichirou_Asuramaru.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(CardSeries.OwariNoSeraph)
            .PostInitialize(data -> data.AddPreview(new Status_Wound(), false));

    public Yuuichirou_Asuramaru()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Mind(1);
        SetAffinity_Poison(1);
        SetAffinity_Dark(1);

        SetDelayed(true);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetDelayed(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new RitualPower(p, 1, true));
    }
}