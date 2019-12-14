package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import eatyourbeets.cards.base.AnimatorCard_Cooldown;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Cirno extends AnimatorCard_Cooldown
{
    public static final String ID = Register(Cirno.class.getSimpleName());

    public Cirno()
    {
        super(ID, 1, AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS, CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

        Initialize(0,5);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        if(EffectHistory.TryActivateLimited(this.cardID)){
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Top.StackPower(new IntangiblePower(enemy, 1));
            }
        }
        if (ProgressCooldown())
        {
            OnCooldownCompleted(p, m);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(3);
        }
    }

    @Override
    protected int GetBaseCooldown() {
        return 1;
    }

    @Override
    protected void OnCooldownCompleted(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.ChannelOrb(new Frost(), true);
        GameActions.Bottom.ChannelOrb(new Frost(), true);
    }
}

