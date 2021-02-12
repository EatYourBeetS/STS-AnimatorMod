package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.actions.orbs.EvokeOrb;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class MarisaKirisame extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MarisaKirisame.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public MarisaKirisame()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);
        SetScaling(0, 0, 0);

        SetSpellcaster();
        SetCooldown(1, 0, this::OnCooldownCompleted);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ChannelOrb(new Lightning(), true);

        GameActions.Bottom.SelectFromHand(name, 1, false)
        .SetOptions(true, true, true)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                GameActions.Top.MoveCard(c, AbstractDungeon.player.hand, AbstractDungeon.player.drawPile).SetDestination(CardSelection.Top);
            }

            GameActions.Bottom.Add(new RefreshHandLayout());
        });

        cooldown.ProgressCooldownAndTrigger(m);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        this.showEvokeValue = cooldown.GetCurrent() == 0;

        return super.GetDamageInfo();
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.EvokeOrb(magicNumber, EvokeOrb.Mode.SameOrb)
                .SetFilter(o -> Lightning.ORB_ID.equals(o.ID));
    }
}

