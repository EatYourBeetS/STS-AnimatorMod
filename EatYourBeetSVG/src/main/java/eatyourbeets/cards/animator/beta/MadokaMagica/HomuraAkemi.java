package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class HomuraAkemi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HomuraAkemi.class).SetSkill(3, CardRarity.RARE, EYBCardTarget.None);

    public HomuraAkemi()
    {
        super(DATA);

        Initialize(0, 0, 3);

        SetExhaust(true);
        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.SFX("POWER_TIME_WARP", 0.05F);
        GameActions.Bottom.VFX(new TimeWarpTurnEndEffect());
        GameActions.Bottom.VFX(new BorderFlashEffect(Color.VIOLET, true));
        GameActions.Bottom.Add(new SkipEnemiesTurnAction());
        GameActions.Bottom.Add(new CreateRandomCurses(magicNumber, p.discardPile));

        if (upgraded)
        {
            GameActions.Bottom.GainIntellect(2, true);
        }

        GameActions.Bottom.Add(new PressEndTurnButtonAction());
    }
}
