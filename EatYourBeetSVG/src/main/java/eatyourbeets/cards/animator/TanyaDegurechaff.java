package eatyourbeets.cards.animator;

import basemod.interfaces.OnStartBattleSubscriber;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnAddedToDeckSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class TanyaDegurechaff extends AnimatorCard implements StartupCard
{
    public static final String ID = Register(TanyaDegurechaff.class.getSimpleName(), EYBCardBadge.Special);

    public TanyaDegurechaff()
    {
        super(ID, 2, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(4, 3);

        SetSynergy(Synergies.YoujoSenki);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new TanyaDegurechaff_Type95(), false);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int discarded = 0;
        for (AbstractCard card : p.hand.getSkills().group)
        {
            GameActionsHelper.AddToBottom(new DiscardSpecificCardAction(card, p.hand));
            discarded += 1;
        }

        if (discarded > 0)
        {
            GameActionsHelper.GainBlock(p, this.magicNumber * discarded);
        }

        GameActionsHelper.AddToBottom(new SFXAction("ATTACK_FIRE"));
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.NONE);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }


    @Override
    public boolean atBattleStartPreDraw()
    {
        if (PlayerStatistics.TryActivateLimited(cardID))
        {
            GameActionsHelper.MakeCardInDrawPile(new TanyaDegurechaff_Type95(), 1, false);

            return true;
        }

        return false;
    }
}