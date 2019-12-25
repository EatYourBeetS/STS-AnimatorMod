package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.actions.cardManipulation.PlayCardFromPile;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class HiiragiTenri extends AnimatorCard_UltraRare
{
    public static final String ID = Register(HiiragiTenri.class.getSimpleName(), EYBCardBadge.Exhaust);

    public HiiragiTenri()
    {
        super(ID, 4, CardType.SKILL, CardTarget.ENEMY);

        Initialize(0, 0, 20);
        SetUpgrade(0, 0, 10);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActions.Bottom.MoveCards(p.drawPile, p.exhaustPile);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(this.magicNumber);

        for (AbstractCard c : p.discardPile.group)
        {
            GameActions.Top.Add(new PlayCardFromPile(c, p.discardPile, true, false, m));
        }

        GameActions.Top.VFX(new OfferingEffect(), 0.1F);
    }
}