package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class Chomusuke extends AnimatorCard_UltraRare
{
    public static final String ID = Register_Old(Chomusuke.class);

    public Chomusuke()
    {
        super(ID, 0, CardType.SKILL, CardTarget.NONE);

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
            AbstractPlayer p = AbstractDungeon.player;
            GameActions.Bottom.GainEnergy(2);
            GameActions.Bottom.MoveCard(this, p.exhaustPile, p.hand)
            .ShowEffect(true, true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActions.Bottom.Draw(1);
    }
}