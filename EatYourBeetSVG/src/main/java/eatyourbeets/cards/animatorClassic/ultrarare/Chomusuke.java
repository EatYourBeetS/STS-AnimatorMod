package eatyourbeets.cards.animatorClassic.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Chomusuke extends AnimatorClassicCard_UltraRare
{
    public static final EYBCardData DATA = Register(Chomusuke.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Chomusuke()
    {
        super(DATA);

        Initialize(0, 0);

        this.series = CardSeries.Konosuba;
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(2);
            GameActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
            .ShowEffect(true, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Draw(1);
    }
}