package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.PlayCardFromPileAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

public class HiiragiTenri extends AnimatorCard_UltraRare
{
    public static final String ID = CreateFullID(HiiragiTenri.class.getSimpleName());

    public HiiragiTenri()
    {
        super(ID, 4, CardType.SKILL, CardTarget.SELF);

        Initialize(0,0, 34);

        this.exhaust = true;

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