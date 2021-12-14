package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.curse.Curse_GriefSeed;
import eatyourbeets.cards.animator.series.MadokaMagica.MadokaKaname;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MadokaKaname_Krimheild extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MadokaKaname_Krimheild.class)
            .SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None)
            .SetSeries(MadokaKaname.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public MadokaKaname_Krimheild()
    {
        super(DATA);

        Initialize(0, 0, 4, 3);
        SetUpgrade(0, 0, 1, 0);
        SetPurge(true);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);

    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainSorcery(magicNumber);
        if (info.TryActivateLimited()) {
            GameUtilities.AddAffinityPowerLevel(Affinity.Blue, secondaryValue);
            GameActions.Bottom.GainResistance(-secondaryValue);
        }
    }
}
