package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.animator.colorless.rare.TanyaDegurechaff;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class TanyaDegurechaff_Type95 extends AnimatorCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff_Type95.class)
            .SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(TanyaDegurechaff.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new OrbCore_Plasma(), false));

    public TanyaDegurechaff_Type95()
    {
        super(DATA);

        Initialize(0, 0);

        SetAffinity_Dark(1);
        SetAffinity_Light(1);

        SetExhaust(true);

        SetAffinityRequirement(Affinity.Light, 3);
        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckAffinity(Affinity.Light) && CheckAffinity(Affinity.Blue))
        {
            GameActions.Bottom.PlayCard(new OrbCore_Plasma(), m);
        }
        else
        {
            GameActions.Bottom.ChannelOrb(new Plasma());
        }
    }
}