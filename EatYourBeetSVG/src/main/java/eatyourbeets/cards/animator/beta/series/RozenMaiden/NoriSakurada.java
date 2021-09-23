package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class NoriSakurada extends AnimatorCard
{
	public static final EYBCardData DATA =
			Register(NoriSakurada.class)
			.SetSkill(0, CardRarity.COMMON, EYBCardTarget.None).SetSeriesFromClassPackage();
	
	public NoriSakurada()
	{
		super(DATA);
		
		Initialize(0 ,0 ,1 ,0);
		SetUpgrade(0 ,0 ,1 ,0);
		SetAffinity_Blue(1, 0, 0);

		SetExhaust(true);
	}


	@Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
		GameActions.Bottom.Draw(magicNumber);

        GameActions.Bottom.SelectFromHand(name, 1, false)
				.SetOptions(false, false, false)
		        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        		.AddCallback(cards ->
        		{
		        	for (AbstractCard card : cards)
					{
						GameActions.Bottom.MoveCard(card, p.hand, p.drawPile)
								.SetDestination(CardSelection.Top);
					}
        		});

        GameActions.Bottom.StackPower(new NoriSakuradaPower(p));
    }

    public static class NoriSakuradaPower extends AnimatorPower
	{
		public NoriSakuradaPower(AbstractCreature owner)
		{
			super(owner,NoriSakurada.DATA);

			this.amount = -1;

			updateDescription();
		}

		@Override
		public void updateDescription()
		{
			description = FormatDescription(0);
		}

		@Override
		public void atEndOfTurn(boolean isPlayer)
		{
			GameActions.Bottom.Reload(NoriSakurada.DATA.Strings.NAME,cards -> {});
			RemovePower();

			super.atEndOfTurn(isPlayer);
		}
	}
/*
    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateLimited(cardID))
        {
            GameAction.Bottom.Draw(secondaryValue)
            .SetFilter(AbstractCard::canUpgrade, true)
            .AddCallback(cards ->
            {
                for (AbstractCard card : cards)
                {
                    if (card.canUpgrade())
                    {
                        card.upgrade();
                        card.flash();
                    }
            });
        }
    }
*/
}

// <DRW-M>, put a card on top of your draw pile.
// Discard ALL your non-Hindrance Ethereal cards at end of this turn.
