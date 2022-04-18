package eatyourbeets.cards.unnamed.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Transcend_Time extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Transcend_Time.class)
            .SetSkill(0, CardRarity.RARE, EYBCardTarget.None);

    public Transcend_Time()
    {
        super(DATA);

        Initialize(0, 0, 3);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetainOnce(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.MoveCards(p.hand, p.drawPile)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.MoveCards(player.discardPile, player.hand, magicNumber)
                .ShowEffect(true, true, 0.15f)
                .SetOrigin(CardSelection.Top);
            }
        });
    }
}