package eatyourbeets.cards.animator.beta.TouhouProject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.ui.PreviewIntent;
import eatyourbeets.utilities.FieldInfo;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

import java.util.ArrayList;
import java.util.Iterator;

public class SatoriKomeiji extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SatoriKomeiji.class).SetPower(0, CardRarity.RARE);

    public SatoriKomeiji()
    {
        super(DATA);

        Initialize(0, 0, 0, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetInnate(true);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.ApplyPower(new SatoriPower(p));
    }

    public static class SatoriPower extends AnimatorPower
    {
        ArrayList<PreviewIntent> previewIntents = new ArrayList<>();

        public SatoriPower(AbstractCreature owner)
        {
            super(owner, SatoriKomeiji.DATA);
            updateDescription();
            updatePreviews();
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(power, target, source);
            GameActions.Bottom.Callback(this::updatePreviews);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();
            GameActions.Bottom.Callback(this::updatePreviews);
        }

        private void updatePreviews() {
            previewIntents.clear();
            for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
                FieldInfo<EnemyMoveInfo> _move = JavaUtilities.GetField("move", AbstractMonster.class);
                PreviewIntent previewIntent = applyPreviewPowers(mo, _move.Get(mo));
                previewIntents.add(previewIntent);
            }
        }

        private PreviewIntent applyPreviewPowers(AbstractMonster source, EnemyMoveInfo move) {
            PreviewIntent previewIntent = new PreviewIntent(source, move);
            if (move.baseDamage > -1) {
                AbstractPlayer target = AbstractDungeon.player;
                int dmg = move.baseDamage;
                float tmp = (float)dmg;
                if (Settings.isEndless && AbstractDungeon.player.hasBlight("DeadlyEnemies")) {
                    float mod = AbstractDungeon.player.getBlight("DeadlyEnemies").effectFloat();
                    tmp *= mod;
                }

                AbstractPower p;
                Iterator var6;
                for(var6 = source.powers.iterator(); var6.hasNext(); tmp = p.atDamageGive(tmp, DamageInfo.DamageType.NORMAL)) {
                    p = (AbstractPower)var6.next();
                }

                for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL)) {
                    p = (AbstractPower)var6.next();
                }

                tmp = AbstractDungeon.player.stance.atDamageReceive(tmp, DamageInfo.DamageType.NORMAL);

                for(var6 = source.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalGive(tmp, DamageInfo.DamageType.NORMAL)) {
                    p = (AbstractPower)var6.next();
                }

                for(var6 = target.powers.iterator(); var6.hasNext(); tmp = p.atDamageFinalReceive(tmp, DamageInfo.DamageType.NORMAL)) {
                    p = (AbstractPower)var6.next();
                }

                dmg = MathUtils.floor(tmp);
                if (dmg < 0) {
                    dmg = 0;
                }

                previewIntent.updateDamage(dmg);
            }
            return previewIntent;
        }

        @Override
        public void renderIcons(SpriteBatch sb, float x, float y, Color c)
        {
            super.renderIcons(sb, x, y, c);

            for (PreviewIntent intent: previewIntents) {
                intent.update();
                intent.render(sb);
            }
        }

        @Override
        public void updateDescription()
        {
            this.description = powerStrings.DESCRIPTIONS[0];
        }
    }
}

