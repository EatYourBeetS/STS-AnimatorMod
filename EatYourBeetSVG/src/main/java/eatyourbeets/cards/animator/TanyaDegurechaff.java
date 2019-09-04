package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import eatyourbeets.interfaces.OnAddedToDeckSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class TanyaDegurechaff extends AnimatorCard implements OnAddedToDeckSubscriber
{
    public static final String ID = CreateFullID(TanyaDegurechaff.class.getSimpleName());

    public TanyaDegurechaff()
    {
        super(ID, 2, CardType.ATTACK, CardColor.COLORLESS, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);

        Initialize(12, 0, 4);

        SetSynergy(Synergies.YoujoSenki);
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
            upgradeDamage(3);
            upgradeMagicNumber(1);
        }
    }

    private AbstractCard preview;

    @Override
    protected AbstractCard GetCardPreview()
    {
        if (preview == null)
        {
            preview = new TanyaDegurechaff_Type95();
        }

        return preview;
    }

    @Override
    public void OnAddedToDeck()
    {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new TanyaDegurechaff_Type95(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
    }
}