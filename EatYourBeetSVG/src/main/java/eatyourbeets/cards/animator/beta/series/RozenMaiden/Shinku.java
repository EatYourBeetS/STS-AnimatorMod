package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Shinku extends AnimatorCard  //TODO
{
    public static final EYBCardData DATA = Register(Shinku.class)
    		.SetAttack(2, CardRarity.RARE).SetSeriesFromClassPackage();
    static
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            DATA.AddPreview(knife, false);
        }
    }

//    private int Index;

    public Shinku()
    {
        super(DATA);

        Initialize(3, 3, 2, 3);
        SetUpgrade(1, 1);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Green(1, 0, 0);

        SetUnique(false, true);
    }
/*
    @Override
    public void initializeDescription()
    {
        super.initializeDescription();
    }
*/
    @Override
    public boolean canUpgrade()
    {
    	return timesUpgraded < secondaryValue;
    }
    
    @Override
    protected void OnUpgrade()
    {
    	if ( timesUpgraded == 1 )
    	{
    		SetHaste(true);
    	}
    	else if ( timesUpgraded == 2 )
    	{
            upgradeMagicNumber(1);
    	}
    	else if ( timesUpgraded == 3 )
    	{
    		SetRetain(true);
    	}
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (IsStarter())
            GameActions.Bottom.Draw(magicNumber);

        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this,m, AttackEffects.SLASH_VERTICAL);

        GameActions.Bottom.MoveCards(p.hand, p.discardPile)
                .AddCallback( cards ->
                        {
                            int addTK = cards.size();

                            if (addTK > 0)
                                GameActions.Bottom.CreateThrowingKnives(addTK);
                        });
    }
/*
    private void ChangeIndex()
    {
        this.Index=timesUpgraded;

        cardText.OverrideDescription(
                JUtils.Format(rawDescription,
                        cardData.Strings.EXTENDED_DESCRIPTION[this.Index]),
                true);
    }*/
}

// Discard your hand. Obtain a Throwing-Knife for each card discarded.
// Opener: Cycle !M! cards.
// Can be Upgraded Thrice.

