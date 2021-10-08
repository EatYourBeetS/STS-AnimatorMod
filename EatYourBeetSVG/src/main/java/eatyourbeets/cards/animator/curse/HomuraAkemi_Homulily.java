package eatyourbeets.cards.animator.curse;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.series.MadokaMagica.SayakaMiki;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class HomuraAkemi_Homulily extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(HomuraAkemi_Homulily.class)
            .SetCurse(-2, EYBCardTarget.None, true)
            .SetSeries(SayakaMiki.DATA.Series)
            .PostInitialize(data -> data.AddPreview(new Curse_GriefSeed(), false));

    public HomuraAkemi_Homulily()
    {
        super(DATA, true);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 0, 0);

        SetAffinity_Dark(2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        GameActions.Bottom.MakeCardInHand(new Curse_GriefSeed());
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.DealDamageAtEndOfTurn(player, player, MathUtils.ceil(player.maxHealth * 0.25f));
        GameActions.Bottom.GainEnergyNextTurn(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
    }
}
