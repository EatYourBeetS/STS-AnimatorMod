package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import eatyourbeets.cards.animator.curse.special.Curse_GriefSeed;
import eatyourbeets.cards.animator.special.HomuraAkemi_Homulilly;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;

public class HomuraAkemi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HomuraAkemi.class)
            .SetSkill(1, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.MadokaMagica_Witch(HomuraAkemi_Homulilly.DATA));
                data.AddPreview(new HomuraAkemi_Homulilly(), true);
                data.AddPreview(new Curse_GriefSeed(), false);
                data.AddPreview(AffinityToken.GetCard(Affinity.Blue), false);
            });

    public HomuraAkemi()
    {
        super(DATA);

        Initialize(0, 2, 2);
        SetUpgrade(0, 3, 0);

        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (info.IsStarter)
        {
            GameActions.Bottom.ObtainAffinityToken(Affinity.Blue, false);
        }

        GameActions.Bottom.ReboundCards(magicNumber);
        GameActions.Bottom.StackPower(new NoDrawPower(p));
    }
}
