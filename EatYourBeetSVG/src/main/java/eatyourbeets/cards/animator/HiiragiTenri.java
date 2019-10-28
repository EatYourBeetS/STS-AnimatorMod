package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.PlayCardFromPileAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

public class HiiragiTenri extends AnimatorCard_UltraRare
{
    public static final String ID = Register(HiiragiTenri.class.getSimpleName(), EYBCardBadge.Drawn);

    public HiiragiTenri()
    {
        super(ID, 4, CardType.SKILL, CardTarget.SELF);

        Initialize(0,0, 34);

        SetExhaust(true);
        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainTemporaryHP(p, p, this.magicNumber);

        for (AbstractCard c : p.discardPile.group)
        {
            GameActionsHelper.AddToTop(new PlayCardFromPileAction(c, p.discardPile, true, false));
        }
        GameActionsHelper.AddToTop(new VFXAction(new OfferingEffect(), 0.1F));
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(10);
        }
    }
}