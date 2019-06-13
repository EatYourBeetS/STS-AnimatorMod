package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.AnimatorResources;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.animator.SoraAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.SoraEffects.SoraEffect;
import patches.AbstractEnums;

public class Sora extends AnimatorCard
{
    public static final String ID = CreateFullID(Sora.class.getSimpleName());

    public final SoraEffect effect;

    public Sora(SoraEffect effect)
    {
        super(AnimatorResources.GetCardStrings(ID), ID + "Alt", AnimatorResources.GetCardImage(ID + "Alt"),
                0, CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, CardRarity.RARE, CardTarget.ALL);
        this.effect = effect;
        //this.damageType = this.damageTypeForTurn = DamageInfo.DamageType.THORNS;
    }

    public Sora()
    {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);

        Initialize(0,0, 2);
        this.effect = null;
        this.isMultiDamage = true;

        SetSynergy(Synergies.NoGameNoLife);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
//        ArrayList<AbstractCard> cards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
//        if (cards.size() > 1 && cards.get(cards.size() - 2).cardID.equals(Shiro.ID))
//        {
//            GameActionsHelper.AddToBottom(new VFXAction(new BorderFlashEffect(Color.GOLD)));
//            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new MasterOfStrategy()));
//            GameActionsHelper.AddToBottom(new WaitRealtimeAction(0.6f));
//        }

        GameActionsHelper.AddToBottom(new WaitAction(1));

        int count = this.magicNumber;
        if (HasActiveSynergy())
        {
            count += 1;
        }

        GameActionsHelper.AddToBottom(new SoraAction(p, count));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public boolean canUpgrade()
    {
        return effect == null && super.canUpgrade();
    }

    public void SetMultiDamage(boolean value)
    {
        this.isMultiDamage = value;
    }
}