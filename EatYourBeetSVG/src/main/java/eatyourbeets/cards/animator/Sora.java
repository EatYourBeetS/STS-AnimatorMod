package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.AnimatorResources;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.actions.SoraAction;
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
        super(AnimatorResources.GetCardStrings(ID), ID, AnimatorResources.GetCardImage(ID + "Alt"),
                0, CardType.SKILL, AbstractEnums.Cards.THE_ANIMATOR, CardRarity.RARE, CardTarget.ALL);
        this.effect = effect;
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