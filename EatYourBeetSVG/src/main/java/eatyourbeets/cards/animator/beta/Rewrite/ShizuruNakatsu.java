package eatyourbeets.cards.animator.beta.Rewrite;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class ShizuruNakatsu extends AnimatorCard implements OnBattleStartSubscriber, OnBattleEndSubscriber {
    public static final EYBCardData DATA = Register(ShizuruNakatsu.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public ShizuruNakatsu() {
        super(DATA);

        Initialize(0, 5, 1);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.DiscardFromHand(name, magicNumber, !upgraded)
        .ShowEffect(!upgraded, !upgraded)
        .SetFilter(c -> c.type == CardType.SKILL)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        });
    }

    @Override
    public void OnBattleStart() {
        CombatStats.onBattleEnd.Subscribe(this);
    }

    @Override
    public void OnBattleEnd() {
        if (player.stance.ID.equals(AgilityStance.STANCE_ID))
        {
            GameActions.Bottom.Add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, false)));
        }
    }
}