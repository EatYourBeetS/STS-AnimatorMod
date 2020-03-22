package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class KyokoSakura extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KyokoSakura.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing);

    public KyokoSakura()
    {
        super(DATA);

        Initialize(7, 0, 1, 1);
        SetUpgrade(3, 0, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActions.Bottom.Draw(magicNumber).AddCallback(m, (enemy, cards) ->
        {
            if (cards.size() > 0)
            {
                for (AbstractCard card : cards)
                {
                    if (GameUtilities.IsCurseOrStatus(card))
                    {
                        for (int i=0; i<secondaryValue; i++)
                        {
                            GameActions.Bottom.ChannelOrb(new Fire(), true);
                        }
                        return;
                    }
                }
            }

            AddToTopOfDrawPile(magicNumber);
        });
    }

    private void AddToTopOfDrawPile(int amount)
    {
        GameActions.Top.SelectFromHand(name, amount, false)
        .SetMessage(GR.Common.Strings.GridSelection.MoveToDrawPile(1))
        .AddCallback(chosenCards ->
        {
            for (AbstractCard chosenCard : chosenCards)
            {
                CardGroup drawPile = player.drawPile;

                GameActions.Top.MoveCard(chosenCard, drawPile)
                .AddCallback(movedCard -> {
                    if (movedCard != null)
                    {
                        JavaUtilities.ChangeIndex(movedCard, drawPile.group, player.drawPile.size());
                    }
                });
            }
        });
    }
}
