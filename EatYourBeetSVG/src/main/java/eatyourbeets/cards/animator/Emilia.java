package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActions;

public class Emilia extends AnimatorCard
{
    public static final String ID = Register(Emilia.class.getSimpleName());

    public Emilia()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0);

        SetEvokeOrbCount(1);
        SetExhaust(true);
        SetSynergy(Synergies.ReZero);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.ChannelOrb(new Frost(), true);
        GameActions.Bottom.Callback(__ ->
        {
            for (AbstractOrb orb : AbstractDungeon.player.orbs)
            {
                if (orb != null && Frost.ORB_ID.equals(orb.ID))
                {
                    GameActions.Bottom.ChannelOrb(new Lightning(), true);
                }
            }
        });
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            SetExhaust(false);
        }
    }
}