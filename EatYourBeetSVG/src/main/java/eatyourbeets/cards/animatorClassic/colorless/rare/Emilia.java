package eatyourbeets.cards.animatorClassic.colorless.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.*;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Emilia extends AnimatorClassicCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Emilia.class).SetSkill(2, CardRarity.RARE, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.ReZero);

    public Emilia()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetEvokeOrbCount(magicNumber);
        SetExhaust(true);
        SetSpellcaster();
    }

    @Override
    protected void OnUpgrade()
    {
        SetEvokeOrbCount(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ChannelOrbs(Frost::new, magicNumber);
        CombatStats.onStartOfTurnPostDraw.Subscribe((Emilia) makeStatEquivalentCopy());
    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        GameEffects.Queue.ShowCardBriefly(this);

        for (AbstractOrb orb : player.orbs)
        {
            if (orb != null && Frost.ORB_ID.equals(orb.ID))
            {
                GameActions.Bottom.ChannelOrb(new Lightning());
            }
        }

        CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
    }
}