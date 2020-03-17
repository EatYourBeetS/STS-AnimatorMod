package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Emilia extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber, Spellcaster
{
    public static final EYBCardData DATA = Register(Emilia.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Emilia()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
        SetSynergy(Synergies.ReZero);
    }

    @Override
    protected void OnUpgrade()
    {
        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.ChannelOrb(new Frost(), true);
        }

        PlayerStatistics.onStartOfTurnPostDraw.Subscribe((Emilia) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameEffects.Queue.ShowCardBriefly(this);

        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && Frost.ORB_ID.equals(orb.ID))
            {
                GameActions.Bottom.ChannelOrb(new Lightning(), true);
            }
        }

        PlayerStatistics.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}