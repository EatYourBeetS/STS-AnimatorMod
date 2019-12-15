package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class MamizouFutatsuiwa extends AnimatorCard
{
    public static final String ID = Register(MamizouFutatsuiwa.class.getSimpleName(), EYBCardBadge.Special);

    public MamizouFutatsuiwa()
    {
        super(ID, 1, CardType.SKILL, AbstractCard.CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0,2);
        SetSynergy(Synergies.TouhouProject, true);
        SetExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(this.magicNumber);
        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetMessage(JavaUtilities.Format(cardData.strings.EXTENDED_DESCRIPTION[0]))
                .AddCallback(cards ->
                {
                    AnimatorCard card = JavaUtilities.SafeCast(cards.get(0), AnimatorCard.class);
                    if (card == null)
                    {
                        return;
                    }
                    card.SetSynergy(Synergies.ANY,true);
                });
        if(EffectHistory.TryActivateSemiLimited(cardID)) {
            GameActions.Top.FetchFromPile(name, 1, AbstractDungeon.player.drawPile, AbstractDungeon.player.discardPile)
                    .SetOptions(false, false)
                    .SetFilter(c -> c instanceof AnimatorCard && (((AnimatorCard) c).anySynergy));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(3);
        }
    }
}
