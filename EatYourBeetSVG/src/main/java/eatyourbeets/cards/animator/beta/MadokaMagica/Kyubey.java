package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Kyubey extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(Kyubey.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None);

    public Kyubey()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(magicNumber);
        GameActions.Bottom.GainEnergy(2);
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        GameActions.Bottom.Add(new CreateRandomCurses(1, player.discardPile));

        return true;
    }
}