package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.OfferingEffect;
import eatyourbeets.actions.animator.GurenAction;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SatoriKomeiji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SatoriKomeiji.class).SetPower(2, CardRarity.RARE);

    public SatoriKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.StackPower(new SatoriPower(p, magicNumber));
    }

    public static class SatoriPower extends AnimatorPower
    {

        public SatoriPower(AbstractCreature owner, int amount)
        {
            super(owner, SatoriKomeiji.DATA);
            this.amount = amount;
            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            GameActions.Top.SelectFromPile(name, amount, player.drawPile)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    player.drawPile.removeCard(c);
                    player.drawPile.addToTop(c);
                }

                GameActions.Bottom.Add(new RefreshHandLayout());
            });

            if (player.drawPile.isEmpty())
            {
                if (player.discardPile.size() > 0)
                {
                    GameActions.Top.Add(new EmptyDeckShuffleAction());
                }
            }
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
            this.enabled = (amount > 0);
        }
    }
}

