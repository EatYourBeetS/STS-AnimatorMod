package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Chomusuke extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Chomusuke.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public Chomusuke()
    {
        super(DATA);

        Initialize(0, 0);

        SetSynergy(Synergies.Konosuba);
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

        if (EffectHistory.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.GainEnergy(2);
            GameActions.Bottom.MoveCard(this, player.exhaustPile, player.hand)
            .ShowEffect(true, true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.Draw(1);
    }
}