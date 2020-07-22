package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.utilities.GameActions;



public class TomoeKashiwaba extends AnimatorCard
{
	public static final EYBCardData DATA =
			Register(TomoeKashiwaba.class)
			.SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None);
	
	public TomoeKashiwaba()
	{
		super(DATA);

        Initialize(0, 3, 0);
		SetUpgrade(0, 2, 0);

		SetMartialArtist();
        SetSynergy(Synergies.RozenMaiden);
	}

	@Override
	public AbstractAttribute GetBlockInfo()
	{
		return null;
	}

	@Override
	public void triggerWhenDrawn()
	{
		if (upgraded)
		{
			GameActions.Bottom.GainForce(1);
		}
	}

	@Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
		if (p.drawPile.size() > 0)
		{
			AbstractCard topCard = p.drawPile.getTopCard();
			if (Synergies.WouldSynergize(this, topCard) && CombatStats.TryActivateSemiLimited(cardID))
			{
				GameActions.Top.GainBlock(block);
				GameActions.Top.Draw(topCard);
			}
		}

		if (!upgraded)
		{
			ForcePower.PreserveOnce();
		}
    }
}
