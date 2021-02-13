package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.curses.Injury;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.CreateRandomCurses;
import eatyourbeets.cards.animator.beta.special.Shirosaki_Laplace;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Shirosaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shirosaki.class)
    		.SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);
    static
    {
        DATA.AddPreview(new Shirosaki_Laplace(), false);
    }
    public Shirosaki()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);
        
        SetExhaust(true);
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
            GameActions.Bottom.ChannelRandomOrb(true);

        GameActions.Bottom.MakeCardInDrawPile(new Shirosaki_Laplace());

        if (HasSynergy())
            GameActions.Bottom.MakeCardInDrawPile(new Injury());
        else
            GameActions.Bottom.Add(new CreateRandomCurses(1, player.drawPile));
    }
}

