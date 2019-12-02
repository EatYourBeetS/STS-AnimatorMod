package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.PlayCardFromPileAction;
import eatyourbeets.cards.AnimatorCard_UltraRare;
import eatyourbeets.cards.Synergies;

public class HiiragiTenri extends AnimatorCard_UltraRare
{
    public static final String ID = Register(HiiragiTenri.class.getSimpleName(), EYBCardBadge.Exhaust);

    public HiiragiTenri()
    {
        super(ID, 4, CardType.SKILL, CardTarget.SELF);

        Initialize(0,0, 20);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (EffectHistory.TryActivateLimited(cardID))
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.AddToBottom(new MoveCardsAction(p.drawPile, p.exhaustPile, Integer.MAX_VALUE));
        }
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