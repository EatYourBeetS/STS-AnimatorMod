package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;

public class HomuraAkemi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HomuraAkemi.class)
            .SetSkill(3, CardRarity.RARE, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public HomuraAkemi()
    {
        super(DATA);

        Initialize(0, 0, 1, 3);

        SetAffinity_Blue(2);
        SetAffinity_Dark(1);

        SetExhaust(true);
        SetEthereal(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.SFX("POWER_TIME_WARP", 0.9F, 1.1f);
        GameActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.VIOLET, true));
        GameActions.Bottom.Add(new SkipEnemiesTurnAction());
        GameActions.Bottom.DrawNextTurn(magicNumber);
        GameActions.Bottom.GainEnergyNextTurn(magicNumber);
        GameActions.Bottom.Add(new CreateRandomCurses(secondaryValue, p.discardPile));
        GameActions.Bottom.Add(new PressEndTurnButtonAction());
    }
}
